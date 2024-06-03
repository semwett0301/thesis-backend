package com.example.services.QueueService;

import com.example.model.entities.db.Route;
import com.example.model.entities.db.RoutePoint;
import com.example.model.exceptions.GptNotWorkingException;
import com.example.model.exceptions.IncorrectGptAnswerException;
import com.example.model.exceptions.RouteNotExistException;
import com.example.model.mappers.RoutePointMapper;
import com.example.model.utils.RouteStatus;
import com.example.repositories.db.RoutePointRepository;
import com.example.repositories.db.RouteRepository;
import com.example.services.GenerateService.GenerateService;
import com.example.utils.QueueUtils.QueueUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class ThesisQueueService implements QueueService {
    private final GenerateService generateService;
    private final QueueUtils queueUtils;

    private final RouteRepository routeRepository;
    private final RoutePointRepository routePointRepository;

    @Override
    // TODO при получении доступа на несколько подключений к Yandex GPT убрать synchronized + провести анализ на эффективность использования потоков
    public synchronized void executeGenerateTask(String routeId) throws RouteNotExistException, GptNotWorkingException {
        startGeneratingProcess(UUID.fromString(routeId.substring(1, routeId.length() - 1)));
    }

    private Route setPendingStatus(Route route) {
        route.setStatus(RouteStatus.PENDING);
        return routeRepository.save(route);
    }

    private void setFailedStatus(Route route) {
        route.setStatus(RouteStatus.FAILED);
        routeRepository.save(route);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    void saveRoute(Route route, List<RoutePoint> routePoints) {
        route.setStatus(RouteStatus.GENERATED);

        routePointRepository.saveAll(routePoints);
        routeRepository.save(route);
    }

    private Route getRoute(UUID routeId) throws RouteNotExistException {
        return routeRepository.findById(routeId).orElseThrow(RouteNotExistException::new);
    }

    private void startGeneratingProcess(UUID routeId) throws RouteNotExistException, GptNotWorkingException {
        var route = getRoute(routeId);

        if (route.getStatus().equals(RouteStatus.CREATED)) {
            try {
                var pendingRoute = setPendingStatus(route);
                var routePoints = getRoutePointsAfterGeneration(pendingRoute);
                saveRoute(pendingRoute, routePoints);
            } catch (GptNotWorkingException | IncorrectGptAnswerException e) {
                log.error(e.getMessage());
                setFailedStatus(route);
            } finally {
                queueUtils.popQueueElement();
            }
        }
    }

    private List<RoutePoint> getRoutePointsAfterGeneration(Route route) throws GptNotWorkingException, IncorrectGptAnswerException {
        var generatedRoutePoints = generateService.generate(route);


        return generatedRoutePoints.stream()
                .map((routePoint) -> RoutePointMapper.createRoutePoint(routePoint, route))
                .toList();
    }
}
