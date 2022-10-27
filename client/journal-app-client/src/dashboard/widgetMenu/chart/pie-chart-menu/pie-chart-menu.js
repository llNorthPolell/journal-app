import React, {useEffect} from 'react';
import {useParams} from 'react-router-dom';
import YDatasetForm from "./y-dataset-form";
import {v4 as uuidv4} from 'uuid';
import {useDashboard} from '../../../../contexts/dashboardContext';
import useSession from '../../../../facades/hooks/useSession';
import useSimpleForm from '../../../../util/hooks/useSimpleForm';


const DefaultPieChartMenu = {
    title: "",
    type: "doughnut",
    labels:[],
    data: [],
    backgroundColor: [],
    hoverOffset: 4
}

function PieChartMenu (props){
    const {journalId} = useParams();
    const [formFields,updateForm,submit,resetForm] = useSimpleForm(DefaultPieChartMenu,);

    const [currentJournal] = useSession(["currentJournal"]);

    const {getOpenDashboardPosition, addNewWidgetConfig} = useDashboard();

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
            type: "pie",
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
                </fieldset>
                
                <fieldset>
                    <button className="btn btn-link" onClick={handleAddYDataset}>Add Another Dataset</button> 

                </fieldset>
                <div className="mb-3">
                    <button className="btn btn-primary" onClick={handleSubmit}>Create</button>
                </div>
            </form>
        </div>
        

    );
}
export default PieChartMenu;