package com.augustxun.safe.mapper;

import com.augustxun.safe.model.dto.user.UserQueryRequest;
import com.augustxun.safe.model.entity.User;
import com.augustxun.safe.model.vo.UserVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author augustxun
 * @description 针对表【user(用户)】的数据库操作Mapper
 * @createDate 2024-04-07 13:37:23
 * @Entity generator.domain.User
 */
public interface UserMapper extends BaseMapper<User> {
    List<UserVO> selectUserVO(@Param("userQueryRequest") UserQueryRequest userQueryRequest);

}




