package com.learn.service.criteria;

import java.io.Serializable;

import com.learn.service.Criteria;

import lombok.Data;
import lombok.NoArgsConstructor;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.StringFilter;

@Data
@NoArgsConstructor
public class UserCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private IntegerFilter id;

    private StringFilter username;

    private StringFilter fullname;

    private InstantFilter dateBirth;

    private StringFilter email;

    private StringFilter phone;

    private IntegerFilter failedAttempt;

    private BooleanFilter isDeleted;

    private InstantFilter lockTime;

    public UserCriteria(UserCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.username = other.username == null ? null : other.username.copy();
        this.fullname = other.fullname == null ? null : other.fullname.copy();
        this.dateBirth = other.dateBirth == null ? null : other.dateBirth.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.failedAttempt = other.failedAttempt == null ? null : other.failedAttempt.copy();
        this.isDeleted = other.isDeleted == null ? null : other.isDeleted.copy();
        this.lockTime = other.lockTime == null ? null : other.lockTime.copy();
    }

    @Override
    public Criteria copy() {
        return new UserCriteria(this);
    }

    public IntegerFilter id() {
        if (id == null) {
            id = new IntegerFilter();
        }
        return id;
    }

    public StringFilter fullname() {
        if (fullname == null) {
            fullname = new StringFilter();
        }
        return fullname;
    }

    public StringFilter username() {
        if (username == null) {
            username = new StringFilter();
        }
        return username;
    }

    public InstantFilter dateBirth() {
        if (dateBirth == null) {
            dateBirth = new InstantFilter();
        }
        return dateBirth;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public StringFilter phone() {
        if (phone == null) {
            phone = new StringFilter();
        }
        return phone;
    }

    public IntegerFilter failedAttempt() {
        if (failedAttempt == null) {
            failedAttempt = new IntegerFilter();
        }
        return failedAttempt;
    }

    public BooleanFilter isDeleted() {
        if (isDeleted == null) {
            isDeleted = new BooleanFilter();
        }
        return isDeleted;
    }

    public InstantFilter lockTime() {
        if (lockTime == null) {
            lockTime = new InstantFilter();
        }
        return lockTime;
    }

}
