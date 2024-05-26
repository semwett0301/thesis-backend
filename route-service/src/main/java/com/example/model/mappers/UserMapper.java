package com.example.model.mappers;

import com.example.model.request.AuthRequest;
import com.example.model.response.UserResponse;
import com.example.model.entities.db.UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserResponse toUserDto(UserInfo userInfo);

    UserInfo toUserInfo(AuthRequest authRequest);
}
