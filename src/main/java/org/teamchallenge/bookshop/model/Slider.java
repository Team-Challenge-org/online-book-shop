package org.teamchallenge.bookshop.model;

import jakarta.persistence.*;
import lombok.*;
import org.teamchallenge.bookshop.dto.SliderDto;

@Getter
@Setter
@Entity
@Table(name = "slider")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Slider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(columnDefinition = "TEXT")
    private String titleImage;
    private String bookName;
    public SliderDto convertToDto() {
        return new SliderDto(id,titleImage,bookName);
    }
}