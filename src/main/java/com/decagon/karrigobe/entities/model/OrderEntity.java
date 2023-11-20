package com.decagon.karrigobe.entities.model;

import com.decagon.karrigobe.entities.enums.OrderStatus;
import com.decagon.karrigobe.utils.CustomIdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_table")
public class OrderEntity extends BaseEntity {

    @Column(name = "tracking_num", length = 22)
    private String trackingNum;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToOne(mappedBy = "orderEntity", cascade = CascadeType.ALL)
    private OrderDescriptionEntity orderDescriptionEntity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH,CascadeType.MERGE
            ,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @OneToMany(mappedBy = "orderEntity", cascade = CascadeType.ALL)
    private List<TrackingLocationEntity> trackingLocationEntities = new ArrayList<>();

    @OneToOne(mappedBy = "orderEntity")        //No cascade to avoid deleting stored amount from transaction entity.
    private TransactionEntity transactionEntity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private DriverTaskEntity driverTaskEntity;


    public void addTrackingLocation(TrackingLocationEntity location){
        trackingLocationEntities.add(location);
        location.setOrderEntity(this);
    }
}
