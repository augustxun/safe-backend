package com.augustxun.safe.controller.admin;

import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.model.dto.checking.CheckingQueryRequest;
import com.augustxun.safe.model.dto.checking.CheckingUpdateRequest;
import com.augustxun.safe.model.entity.Checking;
import com.augustxun.safe.service.CheckingService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

@RestController
@Api(tags = "B端-Checking账户管理接口")

@RequestMapping("admin/checking")
public class CheckingController {
    @Resource
    private CheckingService checkingService;

    /**
     * Checking信息分页查询
     *
     * @param checkingQueryRequest
     * @return
     */
    @Operation(summary = "Checking信息分页查询")
    @ApiIgnore
    @PostMapping("/list/page")
    public BaseResponse<Page<Checking>> listCheckingByPage(@RequestBody CheckingQueryRequest checkingQueryRequest) {
        int current = checkingQueryRequest.getCurrent();
        int size = checkingQueryRequest.getPageSize();
        Page<Checking> checkingPage = checkingService.page(new Page<>(current, size), checkingService.getQueryWrapper(checkingQueryRequest));
        return ResultUtils.success(checkingPage);
    }

    /**
     * 更新
     *
     * @param checkingUpdateRequest
     * @return
     */
    @Operation(summary = "更新Checking表账户数据")
    @PostMapping("/update")
    public BaseResponse<Boolean> updateChecking(@RequestBody CheckingUpdateRequest checkingUpdateRequest) {
        return checkingService.updateChecking(checkingUpdateRequest);
    }
}
