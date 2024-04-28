package com.augustxun.safe.controller.admin;

import com.augustxun.safe.annotation.AuthCheck;
import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.DeleteRequest;
import com.augustxun.safe.common.ErrorCode;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.constant.UserConstant;
import com.augustxun.safe.exception.BusinessException;
import com.augustxun.safe.exception.ThrowUtils;
import com.augustxun.safe.model.dto.user.UserAddRequest;
import com.augustxun.safe.model.dto.user.UserQueryRequest;
import com.augustxun.safe.model.dto.user.UserUpdateRequest;
import com.augustxun.safe.model.entity.Customer;
import com.augustxun.safe.model.entity.User;
import com.augustxun.safe.model.vo.CustomerVO;
import com.augustxun.safe.model.vo.UserVO;
import com.augustxun.safe.service.CustomerService;
import com.augustxun.safe.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.augustxun.safe.service.impl.UserServiceImpl.SALT;

@Slf4j
@RestController
@RequestMapping("admin/user")
@Api(tags = "B端-用户管理接口")
public class AdminUserController {
    @Resource
    private UserService userService;
@Resource
private CustomerService customerService;
    /**
     * 新建用户
     *
     * @param userAddRequest
     * @param request
     * @return
     */
    @Operation(summary = "新建用户")
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest, HttpServletRequest request) {
        if (userAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        // 默认密码 12345678
        String encryptPassword;
        if (StringUtils.isAnyBlank(user.getUserPassword())) {
            String defaultPassword = "12345678";
            encryptPassword = DigestUtils.md5DigestAsHex((SALT + defaultPassword).getBytes());
        } else {
            encryptPassword = DigestUtils.md5DigestAsHex((SALT + user.getUserPassword()).getBytes());
        }
        user.setUserPassword(encryptPassword);
        boolean result = userService.save(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(user.getId());
    }

    /**
     * 删除用户
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @Operation(summary = "删除用户")
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        long id = Long.parseLong(deleteRequest.getId());
        if (deleteRequest == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新用户
     *
     * @param userUpdateRequest
     * @param request
     * @return
     */
    @Operation(summary = "更新用户")
    @PostMapping("/update")
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        // 将 String 转为 Long
        user.setId(Long.parseLong(userUpdateRequest.getId()));
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取用户
     *
     * @param userId
     * @param request
     * @return
     */
    @Operation(summary = "根据 ID 查询用户接口")
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<User> getUserById(@RequestParam String userId, HttpServletRequest request) {
        long id = Long.parseLong(userId);
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }


    /**
     * 用户信息分页查询
     *
     * @param userQueryRequest
     * @param request
     * @return
     */
    @Operation(summary = "用户信息分页查询")
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserVO>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest, HttpServletRequest request) {
        int current = userQueryRequest.getCurrent();
        int size = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(new Page<>(current, size), userService.getQueryWrapper(userQueryRequest));
        List<UserVO> userVOList = userPage.getRecords().stream().map(item -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(item, userVO);
            Long userId = item.getId();
            QueryWrapper<Customer> queryWrapper = new QueryWrapper<Customer>().eq("userId", userId);
            Customer customer = customerService.getOne(queryWrapper);
            CustomerVO customerVO = CustomerVO.objToVo(customer);
            userVO.setCustomerVO(customerVO);
            return userVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(getPages(current,size,userVOList));
    }

    /**
     * 分页函数
     *
     * @param currentPage 当前页数
     * @param pageSize    每一页的数据条数
     * @param list        要进行分页的数据列表
     * @return 当前页要展示的数据
     * @author pochettino
     */
    private Page<UserVO> getPages(Integer currentPage, Integer pageSize, List<UserVO> list) {
        Page<UserVO> page = new Page<>();
        if (list == null) {
            return null;
        }
        int size = list.size();
        if (pageSize > size) {
            pageSize = size;
        }
        if (pageSize != 0) {
            // 求出最大页数，防止currentPage越界
            int maxPage = size % pageSize == 0 ? size / pageSize : size / pageSize + 1;
            if (currentPage > maxPage) {
                currentPage = maxPage;
            }
        }
        // 当前页第一条数据的下标
        int curIdx = currentPage > 1 ? (currentPage - 1) * pageSize : 0;
        List<UserVO> pageList = new ArrayList<>();
        // 将当前页的数据放进pageList
        for (int i = 0; i < pageSize && curIdx + i < size; i++) {
            pageList.add(list.get(curIdx + i));
        }
        page.setCurrent(currentPage).setSize(pageSize).setTotal(list.size()).setRecords(pageList);
        return page;
    }

}
