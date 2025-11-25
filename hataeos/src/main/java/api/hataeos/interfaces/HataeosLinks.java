package api.hataeos.interfaces;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.Link;

public class HataeosLinks {
    private List<Link> links = new ArrayList<>();

    public void addLink(Link link) {
        links.add(link);
    }

    public List<Link> getLinks() {
        return links;
    }
}
