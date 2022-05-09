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
                    display: true,
                    text: 'BPM'
                }
            },
            x:
            {
                title: {
                    display: true,
                    text: 'Date'
                }
            }
        }
    };

    const data = {
        labels:['March 1, 2022', 'March 2, 2022', 'March 3, 2022', 'March 4, 2022', 'March 5, 2022', 'March 8, 2022','March 10, 2022','March 11, 2022'],
        datasets:[
            {
                label: 'Dataset 1',
                data:[120,120,124,124,124,128,128,135],
                borderColor:'#88234d',
                backgroundColor: '#88234d'
            }
        ]
        
    };

    return(
        <div className="card dashboard-widget">
            <h3 className="card-header">Scale Speed</h3>
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