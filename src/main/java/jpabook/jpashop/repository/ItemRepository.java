package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        if (item.getId() == null) {
            em.persist(item);
        } else {
            // merge는 DB에서 영속성컨텍스트에서 똑같은 식별자의 데이터를 찾은 뒤 변경된 값을 다 바꿔치기 하는 것이다.
            Item merge = em.merge(item); // 리턴되는 엔티티는 영속성 컨텍스트에서 관리하고 있는 것을 리턴한다. 파라미터는 영속 상태가 아니기에 완전히 다른 객체이다.


            /**
             ** 주의: 변경 감지 기능을 사용하면 원하는 속성만 선택해서 변경할 수 있지만, 병합을 사용하면 모든 속성이 변경된다.
             * 병합시 값이 없으면 null 로 업데이트 할 위험도 있다. (병합은 모든 필드를 교체한다.) 예를 들어, price값이 null이라면 기존 10000원이 널로 될 수 있다.
             *
             * 그래서 merge가 아닌 변경 감지로 수정해야 한다. 업데이트 칠 필드만 변경해서 갈아끼워 넣어야 한다.
             */
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }



}
