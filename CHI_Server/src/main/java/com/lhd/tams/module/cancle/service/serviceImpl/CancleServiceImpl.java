package com.lhd.tams.module.cancle.service.serviceImpl;

import com.lhd.tams.module.cancle.domain.data;
import com.lhd.tams.module.cancle.domain.dataNewDTO;
import com.lhd.tams.module.cancle.domain.dataVO;
import com.lhd.tams.module.cancle.repository.CancleDao;
import com.lhd.tams.module.cancle.service.CancleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CancleServiceImpl implements CancleService {
    @Resource
    CancleDao cancleDao;
    @Override
    public List<dataVO> listChange(dataNewDTO dataNewDTO) {

        List<dataVO> dataList = new ArrayList<>();

        List<data> entityList = cancleDao.findByTeacherName(dataNewDTO.getTeacherName());
        List<data> entities = new ArrayList<>();
        System.out.println(dataNewDTO.getDate());
        System.out.println(dataNewDTO.getDate1());
        System.out.println(dataNewDTO.getAttend_time());
        System.out.println(dataNewDTO.getFinish_time());
        for (data entity : entityList) {
            if (!((dataNewDTO.getDate()==null && dataNewDTO.getDate1()==null)||
                    ((entity.getDate().after(dataNewDTO.getDate())||
                            (entity.getDate().equals(dataNewDTO.getDate())))
                            && ((entity.getDate().before(dataNewDTO.getDate1()))||
                            (entity.getDate().equals(dataNewDTO.getDate1()))))))
            {
                System.out.println("A");
                continue;
            }
            if (!((dataNewDTO.getAttend_time()==null)||(entity.getAttend_time().equals(dataNewDTO.getAttend_time()))))
            {
                System.out.println("B");
                continue;
            }
            if (!((dataNewDTO.getFinish_time()==null)||(entity.getFinish_time().equals(dataNewDTO.getFinish_time()))))
            {
                System.out.println("C");
                continue;
            }
            System.out.println("?");
            entities.add(entity);
        }
        for (data entity : entities) {
            dataVO vo = new dataVO();
            vo.setId(entity.getId());
            vo.setCourse_name(entity.getCourse_name());
            vo.setTeacher_name(entity.getTeacherName());
            vo.setDate(entity.getDate());
            vo.setAttend_time(entity.getAttend_time());
            vo.setFinish_time(entity.getFinish_time());
            vo.setClassroom_name(entity.getClassroom_name());
            dataList.add(vo);
        }
        return dataList;
    }

    @Override
    public boolean newdata(dataNewDTO dataNewDTO) {
        data entity = new data();
        entity.setId(dataNewDTO.getId());
        entity.setAttend_time(dataNewDTO.getAttend_time());
        entity.setFinish_time(dataNewDTO.getFinish_time());
        entity.setDate(dataNewDTO.getDate());
        entity.setClassroom_name(dataNewDTO.getClassroom_name());
        entity.setTeacherName(dataNewDTO.getTeacherName());
        entity.setCourse_name(dataNewDTO.getCourse_name());
        cancleDao.save(entity); // 假设使用JPA保存实体到数据库
        return true; // 假设新增数据成功
    }

    @Override
    public boolean deletedata(dataNewDTO dataNewDTO) {
        List<data> dataList = cancleDao.findAll();
        System.out.println(dataList.size());
        for (data entity : dataList){
            if (entity.getDate().equals(dataNewDTO.getDate()) &&
                    entity.getAttend_time().equals(dataNewDTO.getAttend_time()) &&
                    entity.getCourse_name().equals(dataNewDTO.getCourse_name())){
                cancleDao.delete(entity);
            }
        }
        return true;
    }
}
