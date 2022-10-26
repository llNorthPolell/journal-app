import React, {useEffect} from 'react';
import {useParams} from 'react-router-dom';
import YDatasetForm from "./y-dataset-form";
import {v4 as uuidv4} from 'uuid';
import {useDashboard} from '../../contexts/dashboardContext';
import useSession from '../../facades/hooks/useSession';
import useSimpleForm from '../../util/hooks/useSimpleForm';


const DefaultLineGraphMenu = {
    title: "",
    xLabel:"",
    yLabel: "",
    xField: "dateOfEntry",
    yList: []
}

function LineGraphMenu (props){
    const {journalId} = useParams();
    const [formFields,updateForm,submit,resetForm] = useSimpleForm(DefaultLineGraphMenu,);

    const [currentJournal] = useSession(["currentJournal"]);

    const {getOpenDashboardPosition, addNewWidgetConfig} = useDashboard();

    useEffect(()=>{
        updateForm({
            yList: [...formFields.yList, {
                id: uuidv4(),
                yColor: "#000",
                yDatasetName: "",
                yTopic: currentJournal?.schemas[0].topic,
                yRecord: currentJournal?.schemas[0].records[0]
            }]
        });
    },[])

    const handleChange = e => {
        updateForm({[e.target.name]: e.target.value});
    }

    const handleAddYDataset = e => {
        e.preventDefault();
        
        const newYDataset = {
            id: uuidv4(),
            yColor: "#000",
            yDatasetName: "",
            yTopic: currentJournal.schemas[0].topic,
            yRecord: currentJournal.schemas[0].records[0]
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
            journal: journalId,
            type: "line-graph",
            position: getOpenDashboardPosition(),
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

        addNewWidgetConfig(config);

        console.log("Save widget as : " + JSON.stringify(config));

        props.close();
    }

    
    const remove = id =>{
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
                            <YDatasetForm id={y.id} key={y.id} data={y} currentJournal={currentJournal} onChange={changeYDataset} remove={remove}></YDatasetForm>
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