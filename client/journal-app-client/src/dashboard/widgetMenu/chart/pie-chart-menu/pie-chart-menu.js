import {v4 as uuidv4} from 'uuid';
import useSimpleForm from '../../../../util/hooks/useSimpleForm';
import DatasetForm from './dataset-form';

const DefaultPieChartMenu = {
    title: "",
    labels:[],
    datasets: [],
    cutout:""
}

function PieChartMenu (props){
    const [formFields,updateForm,submit,] = useSimpleForm(DefaultPieChartMenu,props.submit);

    const handleChange = e => {
        updateForm({[e.target.name]: e.target.value});
    }

    const handleAddDataset = e => {
        e.preventDefault();
        
        const newDataset = {
            id: uuidv4(),
            label: "",
            data: [],
            backgroundColor: [],
            hoverOffset: 4
        }
        updateForm({yList: [...formFields.datasets,newDataset]});
    } 

    const changeDataset = (payload) => {
        updateForm({
            datasets: formFields.datasets.map(ds=>(ds.id === payload.id)?payload:ds)
        });
    }


    const handleSubmit = e => {
        e.preventDefault();

        const config = {
            type: "pie-chart",
            title: formFields.title.trim(),
            labels: {
                x: formFields.xLabel.trim(),
                y: formFields.yLabel.trim()
            },
            data: {
                xValue: formFields.xField,
                yValues: formFields.yList.map(
                    y=> ({
                        id: y.id,
                        backgroundColor: y.yColor,
                        borderColor: y.yColor,
                        label: y.yDatasetName,
                        yTopic: y.yTopic,
                        yRecord: y.yRecord
                    })
                )
            }
        }
        
        submit(config);
        props.close();
    }

    const removeDataset = id =>{
        updateForm({yList: formFields.yList.filter(y=>y.id !== id)});
    }

    return (
        <div className="container-fluid">
            <h2> Pie Chart </h2>

            <form>
                <fieldset>
                    <legend>Labels</legend>
                    <div className="mb-3">
                        <label htmlFor="titleField">Title</label>
                        <input id="titleField" type="text" className="form-control" name="title" value={formFields.title} onChange={handleChange} placeholder="Title"></input>
                    </div>
                </fieldset>
                <fieldset>
                    <legend>Config</legend>
                    <legend>Use Case</legend>
                    <div className="mb-3">
                        <label htmlFor="titleField">Use Case</label>
                        <select id="useCaseField" className="form-select" name="useCase" value={formFields.useCase} onChange={handleChange}>
                            <option value="activities">Activities Over Time</option>
                            <option value="metric">Metric Comparison</option>
                            <option value="progress">Progress vs Target</option>
                        </select>
                    </div>

                    <legend>Dataset</legend>
                    {
                        formFields.datasets.map(y=> 
                            <DatasetForm id={y.id} key={y.id} data={y} schemas={props.schemas} onChange={changeDataset} remove={removeDataset}></DatasetForm>
                        )
                    }

                    <button className="btn btn-link" onClick={handleAddDataset}>Add Another Dataset</button> 

                </fieldset>
                <div className="mb-3">
                    <button className="btn btn-primary" onClick={handleSubmit}>Create</button>
                </div>
            </form>
        </div>
        

    );


}
export default PieChartMenu;

