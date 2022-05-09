import React from 'react';
import {Carousel} from "react-bootstrap";

function PicCarouselWidget(props) {
    return (
        <div className="container dashboard-widget">
            <Carousel indicators>
            {
                props.picList.map(
                    pic => (
                        <Carousel.Item>
                            <img
                                className="d-block carousel-img"
                                src={pic.src}
                                alt={pic.alt}
                            />
                            <Carousel.Caption className="caption">
                                <h3>{pic.label}</h3>
                                <p>{pic.caption}</p>
                            </Carousel.Caption>
                        </Carousel.Item>
                    )
                )
            }
            </Carousel>
        </div>
    );
}
export default PicCarouselWidget;