package org.atlas.platform.orm.jpa.task.repository.order;

import java.util.Date;
import org.atlas.platform.orm.jpa.task.entity.order.JpaOrder;
import org.atlas.service.order.domain.shared.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaOrderRepository extends JpaRepository<JpaOrder, Integer> {

  @Modifying
  @Query("""
        update JpaOrder o
        set o.status = :canceledStatus, o.canceledReason = :canceledReason
        where o.status = :processingStatus
          and o.createdAt < :endDate
    """)
  int cancelOverdueProcessingOrders(
      @Param("canceledStatus") OrderStatus canceledStatus,
      @Param("canceledReason") String canceledReason,
      @Param("processingStatus") OrderStatus pendingStatus,
      @Param("endDate") Date endDate
  );
}
