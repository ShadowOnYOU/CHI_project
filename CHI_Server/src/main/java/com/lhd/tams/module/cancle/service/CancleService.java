package com.lhd.tams.module.cancle.service;

import com.lhd.tams.module.cancle.domain.dataNewDTO;
import com.lhd.tams.module.cancle.domain.dataVO;

import java.util.List;

public interface CancleService {
    List<dataVO> listChange(dataNewDTO dataNewDTO);
    boolean newdata(dataNewDTO dataNewDTO);
    boolean deletedata(dataNewDTO dataNewDTO);
}
