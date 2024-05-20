package org.dacs.quackstagramdatabase.database;

import org.dacs.quackstagramdatabase.annotations.Column;
import org.dacs.quackstagramdatabase.annotations.Entity;
import org.dacs.quackstagramdatabase.annotations.Id;
import org.dacs.quackstagramdatabase.annotations.Incremented;

@Entity(tableName = "TEST")
public class DatabaseTest {
    @Id
    @Incremented
    @Column(name = "ID")
    private Integer id;

    @Column(name = "DATA1")
    private String data1;

    @Column(name = "DATA2")
    private Integer data2;

    public DatabaseTest(String data1, Integer data2) {
        this.data1 = data1;
        this.data2 = data2;
    }

    public DatabaseTest(Integer id, String data1, Integer data2) {
        this.id = id;
        this.data1 = data1;
        this.data2 = data2;
    }

    public Integer getId() {
        return this.id;
    }

    public String getData1() {
        return this.data1;
    }

    public Integer getData2() {
        return this.data2;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setData1(String data1) {
        this.data1 = data1;
    }

    public void setData2(Integer data2) {
        this.data2 = data2;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof DatabaseTest)) return false;
        final DatabaseTest other = (DatabaseTest) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$data1 = this.getData1();
        final Object other$data1 = other.getData1();
        if (this$data1 == null ? other$data1 != null : !this$data1.equals(other$data1)) return false;
        final Object this$data2 = this.getData2();
        final Object other$data2 = other.getData2();
        if (this$data2 == null ? other$data2 != null : !this$data2.equals(other$data2)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof DatabaseTest;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $data1 = this.getData1();
        result = result * PRIME + ($data1 == null ? 43 : $data1.hashCode());
        final Object $data2 = this.getData2();
        result = result * PRIME + ($data2 == null ? 43 : $data2.hashCode());
        return result;
    }

    public String toString() {
        return "DatabaseTest(id=" + this.getId() + ", data1=" + this.getData1() + ", data2=" + this.getData2() + ")";
    }
}
