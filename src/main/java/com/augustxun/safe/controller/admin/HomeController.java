package com.augustxun.safe.controller.admin;

import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.model.dto.home.HomeQueryRequest;
import com.augustxun.safe.model.dto.home.HomeUpdateRequest;
import com.augustxun.safe.model.entity.Home;
import com.augustxun.safe.service.HomeService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

@RestController
@Api(tags = "B端-HomeLoan账户接口")
@RequestMapping("admin/home")
@Slf4j
public class HomeController {
    @Resource
    private HomeService homeService;

    /**
     * HOME账户信息分页查询
     *
     * @param homeQueryRequest
     * @return
     */
    @Operation(summary = "HOME账户信息分页查询")
    @ApiIgnore
    @PostMapping("/list/page")
    public BaseResponse<Page<Home>> listHomeByPage(@RequestBody HomeQueryRequest homeQueryRequest) {
        int current = homeQueryRequest.getCurrent();
        int size = homeQueryRequest.getPageSize();
        Page<Home> homePage = homeService.page(new Page<>(current, size), homeService.getQueryWrapper(homeQueryRequest));
        return ResultUtils.success(homePage);
    }

    /**
     * 更新
     *
     * @param homeUpdateRequest
     * @return
     */
    @Operation(summary = "更新Home表账户数据")
    @PostMapping("/update")
    public BaseResponse<Boolean> updateHome(@RequestBody HomeUpdateRequest homeUpdateRequest) {
        return homeService.updateHome(homeUpdateRequest);

    }
}
