package com.anseong.core.domain.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public abstract class AbstractBaseCeoId implements Serializable {

     private static final long serialVersionUID = -3496651070808570706L;

     private UUID ceoId;

     public AbstractBaseCeoId(UUID ceoId) {
          this.ceoId = ceoId;
     }

     public UUID value() {
          return ceoId;
     }

     public boolean isValid() {
          return ceoId != null;
     }


     @Override
     public boolean equals(Object o) {
          if (o == this)
               return true;
          if (!(o instanceof AbstractBaseCeoId)) {
               return false;
          }
          AbstractBaseCeoId abstractBaseCeoId = (AbstractBaseCeoId) o;
          return Objects.equals(ceoId, abstractBaseCeoId.ceoId);
     }

     @Override
     public int hashCode() {
          return Objects.hashCode(ceoId);
     }

}
