package dev.omedia.boot.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Collection;

@Entity
@Table
@Getter
@Setter
@ToString
public class Unit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "PREDECESSOR_ID")
    private long predecessorId;

    @Column(name = "CONTENT", columnDefinition = "TEXT", nullable = false)
    @NotBlank(message = "Unit's content should not be null")
    private String content;

    @Column(name = "EFFECTIVE_START_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveStartDate;

    @Column(name = "EFFECTIVE_END_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveEndDate;

    @Column(name = "TYPE")
    @Enumerated(value = EnumType.STRING)
    private Type type;

    @Column(name = "PARENT_ID")
    private Long parentId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_UNIT_PARENT_ID_ID"))
    private Collection<Unit> children;
}
