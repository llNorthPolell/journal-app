import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend,
} from 'chart.js';
import { Line } from 'react-chartjs-2';

function LineGraphWidget(props) {
    ChartJS.register(
        CategoryScale,
        LinearScale,
        PointElement,
        LineElement,
        Title,
        Tooltip,
        Legend
    );

    const options = {
        responsive: true,
        plugins: {
            legend: {
                position: "right",
            }
        },
        scales: {
            y:
            {
                title: {
                    display: props.labels !== undefined,
                    text: props.labels !== undefined && props.labels.y !== undefined? props.labels.y : null
                }
            },
            x:
            {
                title: {
                    display: props.labels !== undefined,
                    text: props.labels !== undefined && props.labels.x !== undefined? props.labels.x : null
                }
            }
        }
    };

    const data = {
        labels: props.data.x,
        datasets: props.data.y
    };

    return(
        <div className="card dashboard-widget widget-size-2-2">
            <h3 className="card-header">{props.title}</h3>
            <div className="card-body">
                <Line
                    options={options}
                    data={data}
                ></Line>   
            </div>      
        </div>
    )

}
export default LineGraphWidget