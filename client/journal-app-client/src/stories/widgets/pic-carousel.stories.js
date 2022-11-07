import PicCarouselWidget from '../../pages/dashboard/widgets/pic-carousel/pic-carousel';

export default{
    title: "Widgets/Pic Carousel Widget",
    component: PicCarouselWidget
}

const payload=
    [
        {
            src:"https://firebasestorage.googleapis.com/v0/b/journal-app-75df1.appspot.com/o/testImg.png?alt=media&token=f196b183-09ba-4b43-b603-e9bb36c209ec",
            label:"First slide label",
            caption:"Nulla vitae elit libero, a pharetra augue mollis interdum.",
            alt:"First slide"
        },
        {
            src:"https://firebasestorage.googleapis.com/v0/b/journal-app-75df1.appspot.com/o/Home%20Page%2FTrees.png?alt=media&token=565f2ba4-fdbc-4d95-acba-8b38d82984b3",
            label:"Second slide label",
            caption:"Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            alt:"Second slide"
        },
        {
            src:"https://firebasestorage.googleapis.com/v0/b/journal-app-75df1.appspot.com/o/Home%20Page%2FMountains.png?alt=media&token=071a97c5-9aec-4b30-aab8-df7ef57d3ab8",
            label:"Third slide label",
            caption:"Lorem ipsum dolor sit amet, a pharetra augue mollis interdum.",
            alt:"Third slide"
        }
    ] ;

export const PicCarouselWidgetStory = () => 
    <PicCarouselWidget picList={payload}></PicCarouselWidget>