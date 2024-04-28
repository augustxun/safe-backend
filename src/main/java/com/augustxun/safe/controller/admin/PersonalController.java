package com.augustxun.safe.controller.admin;

import com.augustxun.safe.annotation.AuthCheck;
import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.ErrorCode;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.constant.UserConstant;
import com.augustxun.safe.exception.BusinessException;
import com.augustxun.safe.exception.ThrowUtils;
import com.augustxun.safe.model.dto.personal.PersonalUpdateRequest;
import com.augustxun.safe.model.dto.personal.PersonalQueryRequest;
import com.augustxun.safe.model.entity.Personal;
import com.augustxun.safe.model.entity.Personal;
import com.augustxun.safe.service.PersonalService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api(tags = "B端-PersonalLoan账户管理接口")
@RequestMapping("admin/personal")
public class PersonalController {
    @Resource
    private PersonalService personalService;
    /**
     * Personal账户信息分页查询
     *
     * @param personalQueryRequest
     * @return
     */
    @Operation(summary = "Personal账户信息分页查询")
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Personal>> listPersonalByPage(@RequestBody PersonalQueryRequest personalQueryRequest) {
        int current = personalQueryRequest.getCurrent();
        int size = personalQueryRequest.getPageSize();
        Page<Personal> personalPage = personalService.page(new Page<>(current, size), personalService.getQueryWrapper(personalQueryRequest));
        return ResultUtils.success(personalPage);
    }
    /**
     * 更新
     *
     * @param personalUpdateRequest
     * @return
     */
    @Operation(summary = "更新Personal表账户数据")
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updatePersonal(@RequestBody PersonalUpdateRequest personalUpdateRequest) {
        long acctNo = Long.parseLong(personalUpdateRequest.getAcctNo());

        if (personalUpdateRequest == null || acctNo <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Personal personal = new Personal();
        BeanUtils.copyProperties(personalUpdateRequest, personal);
        personal.setAcctNo(acctNo);
        // 判断是否存在
        Personal oldPersonal = personalService.getById(acctNo);
        ThrowUtils.throwIf(oldPersonal == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = personalService.updateById(personal);
        return ResultUtils.success(result);
    }
}
