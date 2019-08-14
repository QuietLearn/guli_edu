import com.guli.vod.util.AliVodAPIUtils;
import org.junit.Test;

import java.util.*;

/**
 * 视频点播OpenAPI调用示例
 * 以GetVideoPlayAuth接口为例，其他接口请替换相应接口名称及私有参数
 */
public class GetVideoPlayAuthAPITest {

    @Test
    public void testGetVideoPlayAuth() throws Exception{
        //成视频点播OpenAPI私有参数 ，不同API需要修改此方法中的参数
        // 接口私有参数列表, 不同API请替换相应参数
        Map<String, String> privateParams = new HashMap<>();
        // 视频ID
        privateParams.put("VideoId","44f36cd52192492da3c10b788dcea99a");
        // API名称
        privateParams.put("Action", "GetVideoPlayAuth");
//        GetVideoPlayInfo
        //生成公共参数，不需要修改
        Map<String, String> publicParams = AliVodAPIUtils.generatePublicParamters();
        //生成OpenAPI地址，不需要修改
        String URL = AliVodAPIUtils.generateOpenAPIURL(publicParams, privateParams);
        //发送HTTP GET 请求
        AliVodAPIUtils.httpGet(URL);
    }

    @Test
    public void testGetPlayInfo() throws Exception{
        //成视频点播OpenAPI私有参数 ，不同API需要修改此方法中的参数
        // 接口私有参数列表, 不同API请替换相应参数
        Map<String, String> privateParams = new HashMap<>();
        // 视频ID
        privateParams.put("VideoId","da6429e86c674adc90c5aa6ece6ac4a8");
//        request.setResultType("Multiple");
        privateParams.put("ResultType","Multiple");
        // API名称
        privateParams.put("Action", "GetPlayInfo");
//        GetVideoPlayInfo
        //生成公共参数，不需要修改
        Map<String, String> publicParams = AliVodAPIUtils.generatePublicParamters();
        //生成OpenAPI地址，不需要修改
        String URL = AliVodAPIUtils.generateOpenAPIURL(publicParams, privateParams);
        //发送HTTP GET 请求
        AliVodAPIUtils.httpGet(URL);
    }


}