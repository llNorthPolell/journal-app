import LineGraphWidget from '../../dashboard/widgets/chart/line-graph';

export default{
    title: "Widgets/Line Graph Widget",
    component: LineGraphWidget
}

const title="Line Graph";

const labels = {
    x:"X",
    y:"Y"
};

const data = {
    x: ["2022-03-01", "2022-03-02", "2022-03-03", "2022-03-04", "2022-03-05", "2022-03-08","2022-03-10","2022-03-11"],
    y:[
        {
            label: "Target",
            data:[35,47,124,20,90,60,128,56],
            borderColor:"#88234d",
            backgroundColor: "#88234d"
        },
        {
            label: "Actual",
            data:[60,60,60,60,65,65,65,70],
            borderColor:"#232388",
            backgroundColor: "#232388"
        }
    ]
}

export const LineGraphWidgetStory = () => 
    <LineGraphWidget title={title} labels={labels} data={data}></LineGraphWidget>
