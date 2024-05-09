package com.augustxun.safe.controller.admin;

import cn.hutool.json.JSONObject;
import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.ErrorCode;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.mapper.LoanMapper;
import com.augustxun.safe.model.entity.Loan;
import com.augustxun.safe.service.AccountService;
import com.augustxun.safe.service.LoanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController("VisualizeController")
@RequestMapping("admin/visual")
@Api(tags = "B端-数据可视化管理接口")
@Slf4j
public class VisualizeController {
    @Resource
    private LoanMapper loanMapper;
    @ApiOperation(value = "Personal接口")
    @GetMapping("personal")
    BaseResponse<List<Object>> queryPersonalData() {
        List<Object> list = loanMapper.queryPersonalData();
        if (list == null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR,"数据不存在");
        }
        return ResultUtils.success(list);
    }
    @ApiOperation(value = "Home接口")
    @GetMapping("home")
    BaseResponse<List<Object>> queryHomeData() {
        List<Object> list = loanMapper.queryHomeData();
        if (list == null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR,"数据不存在");
        }
        return ResultUtils.success(list);
    }
}
