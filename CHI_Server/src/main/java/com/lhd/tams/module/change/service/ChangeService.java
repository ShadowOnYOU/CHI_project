package com.lhd.tams.module.change.service;

import com.lhd.tams.module.change.domain.Change;
import com.lhd.tams.module.change.domain.ChangeNewDTO;
import com.lhd.tams.module.change.domain.ChangeVO;
import com.lhd.tams.module.coursescheduling.model.dto.CourseSchedulingSaveDTO;

import java.util.List;

public interface ChangeService {
    List<ChangeVO> listChange(ChangeNewDTO changeNewDTO);

    boolean newChange(ChangeNewDTO changeNewDTO);
}
