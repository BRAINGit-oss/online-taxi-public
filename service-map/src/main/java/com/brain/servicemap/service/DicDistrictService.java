package com.brain.servicemap.service;

import com.brain.servicemap.mapper.DicDistrictMapper;
import com.brain.servicemap.remote.MapDicDistrictClient;
import com.brain.servicepassengeruser.internalcommon.constant.AmapConfigurationConstants;
import com.brain.servicepassengeruser.internalcommon.constant.CommonStatusEnum;
import com.brain.servicepassengeruser.internalcommon.dto.DicDistrict;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DicDistrictService {

    @Autowired
    DicDistrictMapper dicDistrictMapper;

    @Autowired
    private MapDicDistrictClient mapDicDistrictClient;

    public ResponseResult initDicDistrict(String keywords){
        //请求地图
        String dicDistrict = mapDicDistrictClient.dicDistrict(keywords);
        System.out.println(dicDistrict);

        //解析行政区域代码 行政区级别包括：国家、省/直辖市、市、区/县4个级别 0 1 2 3
        //fromObject获取最外层 -> districts JSONArray->对列表进行遍历读取adcode name 父级别？ level=[contry,province,city,district,street]
        JSONObject jsonObjectResult = JSONObject.fromObject(dicDistrict);
        int status = jsonObjectResult.getInt(AmapConfigurationConstants.STATUS);
        if(status!=1){
            return ResponseResult.fail(CommonStatusEnum.DISTRICT_ERROR.getCode(),CommonStatusEnum.DISTRICT_ERROR.getValue());
        }
        JSONArray contryJsonArray = jsonObjectResult.getJSONArray(AmapConfigurationConstants.DISTRICTS);
        for(int contry =0 ;contry<contryJsonArray.size();contry++){
            JSONObject contryJson = contryJsonArray.getJSONObject(contry);
            String contryAddreeCode = contryJson.getString(AmapConfigurationConstants.ADCODE);
            String contryName = contryJson.getString(AmapConfigurationConstants.NAME);
            String contryLevel = contryJson.getString(AmapConfigurationConstants.LEVEL);
            String contryParentAddressCode = "0";

            insertDicDistrict(contryAddreeCode,contryName,contryLevel,contryParentAddressCode);

            JSONArray provinceJsonArray = contryJson.getJSONArray(AmapConfigurationConstants.DISTRICTS);
            for(int provinvce =0 ;provinvce<provinceJsonArray.size();provinvce++){
                JSONObject provinceJson = provinceJsonArray.getJSONObject(provinvce);
                String provinceAddreeCode = provinceJson.getString(AmapConfigurationConstants.ADCODE);
                String provinceName = provinceJson.getString(AmapConfigurationConstants.NAME);
                String provinceLevel = provinceJson.getString(AmapConfigurationConstants.LEVEL);
                String provinceParentAddressCode = contryAddreeCode;

                insertDicDistrict(provinceAddreeCode,provinceName,provinceLevel,provinceParentAddressCode);

                JSONArray cityJsonArray = provinceJson.getJSONArray(AmapConfigurationConstants.DISTRICTS);
                for(int city =0 ;city<cityJsonArray.size();city++){
                    JSONObject cityJson = cityJsonArray.getJSONObject(city);
                    String cityAddreeCode = cityJson.getString(AmapConfigurationConstants.ADCODE);
                    String cityName = cityJson.getString(AmapConfigurationConstants.NAME);
                    String cityLevel = cityJson.getString(AmapConfigurationConstants.LEVEL);
                    String cityParentAddressCode = provinceAddreeCode;

                    insertDicDistrict(cityAddreeCode,cityName,cityLevel,cityParentAddressCode);

                    JSONArray districtJsonArray = cityJson.getJSONArray(AmapConfigurationConstants.DISTRICTS);
                    for(int d =0 ;d<districtJsonArray.size();d++){
                        JSONObject districtJson = districtJsonArray.getJSONObject(d);
                        String districtAddreeCode = districtJson.getString(AmapConfigurationConstants.ADCODE);
                        String districtName = districtJson.getString(AmapConfigurationConstants.NAME);
                        String districtLevel = districtJson.getString(AmapConfigurationConstants.LEVEL);
                        String districtParentAddressCode = cityAddreeCode;

                        //如果地址为street则continue；
                        if(districtLevel.trim().equals(AmapConfigurationConstants.STREET)){
                            continue;
                        }

                        insertDicDistrict(districtAddreeCode,districtName,districtLevel,districtParentAddressCode);

                    }
                }
            }
        }


        //写入数据库

        return ResponseResult.success("");
    }

    public void insertDicDistrict(String addreeCode,String addreeName,String level,String parentAddressCode){
        DicDistrict dicDistrict = new DicDistrict();
        dicDistrict.setAddressCode(addreeCode);
        dicDistrict.setAddressName(addreeName);
        String levelId = generateLevel(level);
        dicDistrict.setLevel(Integer.valueOf(levelId));
        dicDistrict.setParentAddressCode(parentAddressCode);
        dicDistrictMapper.insert(dicDistrict);
    }

    public String generateLevel(String contryLevel){
//        level=[contry,province,city,district,street]
        String intContryLevel = "0";
        if(contryLevel.trim().equals("country")){
            intContryLevel = "0";
        }else if(contryLevel.trim().equals("province")){
            intContryLevel = "1";
        }else if(contryLevel.trim().equals("city")){
            intContryLevel = "2";
        }else if(contryLevel.trim().equals("district")){
            intContryLevel = "3";
        }
        return intContryLevel;
    }
}
