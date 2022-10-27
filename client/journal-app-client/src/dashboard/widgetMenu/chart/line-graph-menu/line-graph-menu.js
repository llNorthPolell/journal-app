import {v4 as uuidv4} from 'uuid';
import useSimpleForm from '../../../../util/hooks/useSimpleForm';
import YDatasetForm from './y-dataset-form';

const DefaultLineGraphMenu = {
    title: "",
    xLabel:"",
    yLabel: "",
    xField: "dateOfEntry",
    yList: []
}

function LineGraphMenu (props){
    const [formFields,updateForm,submit,resetForm] = useSimpleForm(DefaultLineGraphMenu,props.submit);

    const handleChange = e => {
        updateForm({[e.target.name]: e.target.value});
    }

    const handleAddYDataset = e => {
        e.preventDefault();
        
        const newYDataset = {
            id: uuidv4(),
            yColor: "#000",
            yDatasetName: "",
            yTopic: (props.schemas && props.schemas.length > 0)? props.schemas[0].topic : "",
            yRecord: (props.schemas && props.schemas.length > 0)? props.schemas[0].records[0] : ""
        }
        updateForm({yList: [...formFields.yList,newYDataset]});
    } 

    const changeYDataset = (payload) => {
        updateForm({
            yList: formFields.yList.map(y=>(y.id === payload.id)?payload:y)
        });
    }


    const handleSubmit = e => {
        e.preventDefault();

        const config = {
            type: "line-graph",
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

    const removeYDataset = id =>{
        updateForm({yList: formFields.yList.filter(y=>y.id !== id)});
    }

    return (
        <div className="container-fluid">
            <h2> Line Graph </h2>

            <form>
                <fieldset>
                    <legend>Labels</legend>
                    <div className="mb-3">
                        <label htmlFor="titleField">Title</label>
                        <input id="titleField" type="text" className="form-control" name="title" value={formFields.title} onChange={handleChange} placeholder="Title"></input>
                    </div>
                    <div className="mb-3">
                        <label htmlFor="xLabelField">X</label>
                        <input id="xLabelField" type="text" className="form-control" name="xLabel" value={formFields.xLabel} onChange={handleChange} placeholder="X"></input>
                    </div>
                    <div className="mb-3">
                        <label htmlFor="yLabelField">Y</label>
                        <input id="yLabelField" type="text" className="form-control" name="yLabel" value={formFields.yLabel} onChange={handleChange} placeholder="Y"></input>
                    </div>
                </fieldset>
                
                <fieldset>
                    <legend>Config</legend>
                    <fieldset>
                        <legend>X</legend>
                        <div className="mb-3 col">
                            <label htmlFor="xFieldNameField">Field Name</label>
                            <select id="xFieldNameField" className="form-select" name="xField" value={formFields.xField} onChange={handleChange}>
                                <option value="dateOfEntry">Date of Entry</option>
                            </select>
                        </div>
                    </fieldset>

                    <legend>Y</legend>
                    {
                        formFields.yList.map(y=> 
                            <YDatasetForm id={y.id} key={y.id} data={y} schemas={props.schemas} onChange={changeYDataset} remove={removeYDataset}></YDatasetForm>
                        )
                    }

                    <button className="btn btn-link" onClick={handleAddYDataset}>Add Another Dataset</button> 

                </fieldset>
                <div className="mb-3">
                    <button className="btn btn-primary" onClick={handleSubmit}>Create</button>
                </div>
            </form>
        </div>
        

    );


}
export default LineGraphMenu;

