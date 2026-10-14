package com.anseong.core.infrastructure.repository.hibernate.event;

import org.springframework.stereotype.Repository;

import com.anseong.core.domain.model.event.Event;
import com.anseong.core.domain.model.event.repository.EventCommandRepository;
import com.anseong.core.infrastructure.repository.hibernate.HibernateSupport;

import jakarta.persistence.EntityManager;

@Repository
class HibernateEventCommandRepository extends HibernateSupport<Event> implements EventCommandRepository {

     HibernateEventCommandRepository(EntityManager entityManager) {
          super(entityManager);
     }

     @Override
     public void save(Event event) {
          save(event);
     }
}
