import PieChartWidget from '../../pages/dashboard/widgets/chart/pie-chart';

export default{
    title: "Widgets/Pie Chart Widget",
    component: PieChartWidget
}

const title="Pie Chart";

const labels = [
    'Red',
    'Blue',
    'Yellow'
];

const datasets = [{
    label: 'My First Dataset',
    data: [300, 50, 100],
    backgroundColor: [
        'rgb(255, 99, 132)',
        'rgb(54, 162, 235)',
        'rgb(255, 205, 86)'
    ],
    hoverOffset: 4
}];

const cutout = 50;

export const PieChartWidgetStory = () => 
    <PieChartWidget title={title} labels={labels} datasets={datasets} cutout={cutout}></PieChartWidget>
