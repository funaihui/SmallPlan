package com.wizardev.smallplanmvp.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * author : wizardev
 * e-mail : wizarddev@163.com
 * time   : 2017/10/26
 * desc   :
 * version: 1.0
 */
@Entity
public class Plan {
    //这个注解就表明下面那个id是个主键，必须用Long，autoincrement=true自增
    @Id(autoincrement = true)
    Long id;
    @NotNull
    private String something;
    @NotNull
    private String time;
    @NotNull
    private int flag;
    @Generated(hash = 1919211665)
    public Plan(Long id, @NotNull String something, @NotNull String time,
            int flag) {
        this.id = id;
        this.something = something;
        this.time = time;
        this.flag = flag;
    }
    @Generated(hash = 592612124)
    public Plan() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getSomething() {
        return this.something;
    }
    public void setSomething(String something) {
        this.something = something;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public int getFlag() {
        return this.flag;
    }
    public void setFlag(int flag) {
        this.flag = flag;
    }
}
