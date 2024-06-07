package org.sku.zero.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notice")
@SQLDelete(sql = "UPDATE notice SET is_available = 0 WHERE id = ?")
@SQLRestriction("is_available = 1")
@Getter
@DynamicUpdate
@DynamicInsert
public class Notice extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", updatable = false, insertable = false)
    private Member member;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "view_count")
    private Integer viewCount;

    @Column(name = "is_available")
    private Integer isAvailable;

    @Builder
    public Notice(String title, String content, Long memberId) {
        this.title = title;
        this.content = content;
        this.memberId = memberId;
    }

    public void editNotice(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void increaseViewCount() {
        ++this.viewCount;
    }
}
