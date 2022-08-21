import React, {useState} from 'react';
import useSimpleState from "../../util/hooks/useSimpleState";
import YDatasetForm from "./y-dataset-form";
import {v4 as uuidv4} from 'uuid';
import listUtil from '../../util/functions/list-util';
import {useDashboard} from '../../contexts/dashboardContext';

function LineGraphMenu (props){
    const [title,, handleChangeTitle] = useSimpleState(props.data? props.data.title : "");
    const [xLabel,, handleChangeXLabel] = useSimpleState(props.data? props.data.xLabel : "");
    const [yLabel,, handleChangeYLabel] = useSimpleState(props.data? props.data.yLabel : "");
    const [xField,, handleChangeXField] = useSimpleState("dateOfEntry");

    const {currentJournal,getOpenDashboardPosition, addNewWidgetConfig} = useDashboard();

    const [yList, setYList] = useState(props.data? props.data.yList : [{
        id: uuidv4(),
        backgroundColor: "#000",
        borderColor: "#000",
        label: "",
        topic: (currentJournal.schemas.length>0)?currentJournal.schemas[0].topic:"",
        record:  (currentJournal.schemas.length>0 && currentJournal.schemas[0].records.length>0)?currentJournal.schemas[0].records[0] : ""
    }]);

    const handleAddYDataset = e => {
        e.preventDefault();
        
        listUtil(yList,setYList,{type:"INSERT", payload: {
            id: uuidv4(),
            backgroundColor: "#000",
            borderColor: "#000",
            label: "",
            topic: currentJournal.schemas[0].topic,
            record: currentJournal.schemas[0].records[0]
        }});
    } 

    const handleSubmit = e => {
        e.preventDefault();

        const config = {
            journal: currentJournal.key,
            type: "line-graph",
            position: getOpenDashboardPosition(),
            title: title,
            labels: {
                x: xLabel,
                y: yLabel
            },
            data: {
                xValue: xField,
                yValues: yList
            }
        }

        addNewWidgetConfig(config);

        console.log("Save widget as : " + JSON.stringify(config));

        props.close();
    }

    return (
        <div className="container-fluid">
            <h2> Line Graph </h2>

            <form>
                <fieldset>
                    <legend>Labels</legend>
                    <div className="mb-3">
                        <label htmlFor="titleField">Title</label>
                        <input id="titleField" type="text" className="form-control" name="title" value={title} onChange={handleChangeTitle} placeholder="Title"></input>
                    </div>
                    <div className="mb-3">
                        <label htmlFor="xLabelField">X</label>
                        <input id="xLabelField" type="text" className="form-control" name="xLabel" value={xLabel} onChange={handleChangeXLabel} placeholder="X"></input>
                    </div>
                    <div className="mb-3">
                        <label htmlFor="yLabelField">Y</label>
                        <input id="yLabelField" type="text" className="form-control" name="yLabel" value={yLabel} onChange={handleChangeYLabel} placeholder="Y"></input>
                    </div>
                </fieldset>
                
                <fieldset>
                    <legend>Config</legend>
                    <fieldset>
                        <legend>X</legend>
                        <div className="mb-3 col">
                            <label htmlFor="xFieldNameField">Field Name</label>
                            <select id="xFieldNameField" className="form-select" name="xField" value={xField} onChange={handleChangeXField} placeholder="Field Name">
                                <option value="dateOfEntry">Date of Entry</option>
                            </select>
                        </div>
                    </fieldset>

                    <legend>Y</legend>
                    {
                        yList.map(y=> 
                            <YDatasetForm id={y.id} yList={yList} setYList={setYList}></YDatasetForm>
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