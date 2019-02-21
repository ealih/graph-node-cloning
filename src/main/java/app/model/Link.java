package app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Link {

    @JsonIgnore
    public static final int INVALID_REF = -1;

    private int from = INVALID_REF;
    private int to = INVALID_REF;

    public Link(){}

    public Link(int from, int to) {
        this.from = from;
        this.to = to;
    }

    @JsonProperty("from")
    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    @JsonProperty("to")
    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Link link = (Link) o;
        return from == link.from &&
                to == link.to;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    @Override
    public String toString() {
        return "app.model.Link{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }
}
