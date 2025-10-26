package com.josephhieu.quanlyquancaphe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ban")
@Getter
@Setter
@NoArgsConstructor
public class Ban {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "MaBan", length = 36, nullable = false)
    private String maBan;

    @Column(name = "TenBan", length = 50, nullable = false)
    private String tenBan;

    @Column(name = "TinhTrang", length = 50, nullable = false)
    private String tinhTrang;
}
