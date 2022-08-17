import LineGraphMenu from "../widgets/line-graph-menu";
import PicCarouselMenu from '../widgets/pic-carousel-menu'

function WidgetMenuForms(props){

    switch (props.menu){
        case "line-graph":
            return <LineGraphMenu></LineGraphMenu>
        case "pic-carousel":
            return <PicCarouselMenu></PicCarouselMenu>
        default:
            return <></>
    }    
    
}
export default WidgetMenuForms;