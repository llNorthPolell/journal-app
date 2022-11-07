import Chart from 'chart.js/auto';
import { Pie } from 'react-chartjs-2';

function PieChartWidget(props) {
    const options = {
        maintainAspectRatio: false,
        responsive: true,
        cutout: props.cutout +'%'
    };

    const data = {
        labels: props.labels,
        datasets: props.datasets
    };

    return(
        <div className="card dashboard-widget widget-size-2-2">
            <h3 className="card-header">{props.title}</h3>
            <div className="card-body">
                <div className="container chart-container">
                    <Pie
                        options={options}
                        data={data}
                    ></Pie>   
                </div>

            </div>      
        </div>
    )

}
export default PieChartWidget