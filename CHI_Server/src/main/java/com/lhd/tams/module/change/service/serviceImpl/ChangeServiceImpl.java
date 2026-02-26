package com.lhd.tams.module.change.service.serviceImpl;

import com.lhd.tams.module.change.domain.Change;
import com.lhd.tams.module.change.domain.ChangeNewDTO;
import com.lhd.tams.module.change.domain.ChangeVO;
import com.lhd.tams.module.change.service.ChangeService;
import com.lhd.tams.module.change.repository.ChangeDao;
import com.lhd.tams.module.coursescheduling.model.dto.CourseSchedulingSaveDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChangeServiceImpl implements ChangeService {

    @Resource
    private ChangeDao changeDao; // 注入 ChangeDao 对象

    @Override
    public List<ChangeVO> listChange(ChangeNewDTO changeNewDTO) {
        List<Change> changeList = changeDao.findByTeacherName(changeNewDTO.getTeacherName()); // 查询所有的 Change 实体
        List<Change> changes = new ArrayList<>();
        for (Change change : changeList){
            if (!((changeNewDTO.getOld_date()==null && changeNewDTO.getNew_date()==null)||
                    ((change.getOld_date().after(changeNewDTO.getOld_date())||
                            (change.getOld_date().equals(changeNewDTO.getOld_date())))
                            && ((change.getOld_date().before(changeNewDTO.getNew_date()))||
                            (change.getOld_date().equals(changeNewDTO.getNew_date()))))))
            {
                continue;
            }
            if (!((changeNewDTO.getOld_date1()==null && changeNewDTO.getNew_date1()==null)||
                    ((change.getNew_date().after(changeNewDTO.getOld_date1())||
                            (change.getNew_date().equals(changeNewDTO.getOld_date1())))
                            && ((change.getNew_date().before(changeNewDTO.getNew_date1()))||
                            (change.getNew_date().equals(changeNewDTO.getNew_date1()))))))
            {
                continue;
            }
            if (!((changeNewDTO.getOld_start_time()==null)||(change.getOld_start_time().equals(changeNewDTO.getOld_start_time()))))
            {
                continue;
            }
            if (!((changeNewDTO.getOld_end_time()==null || change.getOld_end_time().equals(changeNewDTO.getOld_end_time())))){
                continue;
            }
            if (!((changeNewDTO.getNew_start_time()==null)||(change.getNew_start_time().equals(changeNewDTO.getNew_start_time()))))
            {
                continue;
            }
            if (!((changeNewDTO.getNew_end_time()==null || change.getNew_end_time().equals(changeNewDTO.getNew_end_time())))){
                continue;
            }
            changes.add(change);
        }
        return changes.stream().map(this::convertToVO).collect(Collectors.toList()); // 返回 ChangeVO 列表
    }

    @Override
    public boolean newChange(ChangeNewDTO changeNewDTO) {
        Change change = new Change(); // 创建一个新的 Change 实体
        change.setCourse_name(changeNewDTO.getCourse_name()); // 复制 course_name 属性
        change.setTeacherName(changeNewDTO.getTeacherName()); // 复制 teacher_name 属性
        change.setOld_date(changeNewDTO.getOld_date()); // 复制 old_date 属性
        change.setNew_date(changeNewDTO.getNew_date()); // 复制 new_date 属性
        change.setOld_start_time(changeNewDTO.getOld_start_time()); // 复制 old_start_time 属性
        change.setOld_end_time(changeNewDTO.getOld_end_time()); // 复制 old_end_time 属性
        change.setNew_start_time(changeNewDTO.getNew_start_time()); // 复制 new_start_time 属性
        change.setNew_end_time(changeNewDTO.getNew_end_time()); // 复制 new_end_time 属性
        change.setOld_classroom_name(changeNewDTO.getOld_classroom_name()); // 复制 old_classroom_name 属性
        change.setNew_classroom_name(changeNewDTO.getNew_classroom_name()); // 复制 new_classroom_name 属性
        change.setApproval_status(changeNewDTO.getApproval_status()); // 复制 approval_status 属性
        changeDao.save(change); // 保存 Change 实体
        return true; // 返回 true 表示成功
    }


    // 定义一个辅助方法，用于将 Change 实体转换成 ChangeVO 对象
    private ChangeVO convertToVO(Change change) {
        ChangeVO changeVO = new ChangeVO(); // 创建一个新的 ChangeVO 对象
        changeVO.setId(change.getId()); // 复制 id 属性
        changeVO.setCourse_name(change.getCourse_name()); // 复制 course_name 属性
        changeVO.setTeacher_name(change.getTeacherName()); // 复制 teacher_name 属性
        changeVO.setOld_date(change.getOld_date()); // 复制 old_date 属性
        changeVO.setNew_date(change.getNew_date()); // 复制 new_date 属性
        changeVO.setOld_start_time(change.getOld_start_time()); // 复制 old_start_time 属性
        changeVO.setOld_end_time(change.getOld_end_time()); // 复制 old_end_time 属性
        changeVO.setNew_start_time(change.getNew_start_time()); // 复制 new_start_time 属性
        changeVO.setNew_end_time(change.getNew_end_time()); // 复制 new_end_time 属性
        changeVO.setOld_classroom_name(change.getOld_classroom_name()); // 复制 old_classroom_name 属性
        changeVO.setNew_classroom_name(change.getNew_classroom_name()); // 复制 new_classroom_name 属性
        changeVO.setApproval_status(change.getApproval_status()); // 复制 approval_status 属性
        return changeVO; // 返回 ChangeVO 对象
    }
}
