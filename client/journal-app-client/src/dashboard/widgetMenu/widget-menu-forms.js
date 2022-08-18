import LineGraphMenu from "../widgets/line-graph-menu";
import PicCarouselMenu from '../widgets/pic-carousel-menu'

function WidgetMenuForms(props){

    switch (props.menu){
        case "line-graph":
            return <LineGraphMenu close={props.close}></LineGraphMenu>
        case "pic-carousel":
            return <PicCarouselMenu close={props.close}></PicCarouselMenu>
        default:
            return <></>
    }    
    
}
export default WidgetMenuForms;