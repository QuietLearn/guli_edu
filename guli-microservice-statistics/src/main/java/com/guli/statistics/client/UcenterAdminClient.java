package com.guli.statistics.client;

import com.guli.core.common.R;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
@FeignClient("GULI-UCENTER")
@RequestMapping("/admin/ucenter/member")
public interface UcenterAdminClient {

    /**
     * 注意：一定要写成 @PathVariable("day")，圆括号中的"day"不能少
     * @param day
     * @return
     */
    @GetMapping(value = "count-register/{day}")
    R registerCount( @PathVariable("day") String day);
}
