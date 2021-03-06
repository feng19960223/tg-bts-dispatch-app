package com.turingoal.bts.dispatch.bean;

import java.util.Date;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import lombok.Data;

/**
 * 车号，数据库专用
 */
@Data
@Entity
public class TrainSetDb {
    @Id
    public long entityId; // objectBox 的id需要为Long 类型
    public String id;
    public String codeNum;
    public Integer trainSetType;
    public String trainSetModel;
    public Integer trainSetLengthType;
    public Integer trainSetStaffType;
    public Date productionDate;
    public String description;
    public Integer sortOrder;
    public Integer enabled;

    public TrainSetDb() {

    }
}
