import React, {useState,useRef} from 'react';
import RemoveButton from '../../../../util/components/remove-button';
import useSimpleChildForm from '../../../../util/hooks/useSimpleChildForm';

function YDatasetForm (props){

    const [formFields,updateForm,saveYDataset,] = useSimpleChildForm(props.data, props.onChange);

    const yTopicList= useRef(props.schemas?.map(schema=>schema.topic));
    const [yRecordList,setYRecordList]= useState((props.schemas && props.schemas.length>0)? props.schemas[0].records : []);


    const handleChange = e => {
        updateForm({[e.target.name]: e.target.value});
    }

    const handleChangeYTopic = e => {
        const newRecordsList = props.schemas.find(schema=>schema.topic===e.target.value).records;
        console.log("New Record List: "+ JSON.stringify(newRecordsList))
        updateForm({yTopic:e.target.value, yRecord:newRecordsList[0]});
        setYRecordList(newRecordsList);
    }

    return (
        <fieldset className="lineGraphYForm">
            <div className="mb-3">
                <RemoveButton id={props.key} className="btn btn-secondary" remove={()=>props.remove(formFields.id)}></RemoveButton> 
            </div>
            <div className="mb-3">
                <label htmlFor="yColorField">Color</label>
                <input id="yColorField" type="color" className="form-control" name="yColor" value={formFields.yColor} onChange={handleChange} onBlur={saveYDataset} placeholder="#000"></input>
            </div>
            <div className="mb-3">
                <label htmlFor="yDatasetNameField">Dataset Name</label>
                <input id="yDatasetNameField" type="text" className="form-control" name="yDatasetName" value={formFields.yDatasetName} onChange={handleChange}  onBlur={saveYDataset} placeholder="Dataset Name"></input>
            </div>
            <div className="row">
                <div className="mb-3 col">
                    <label htmlFor="yTopicField">Topic</label>
                    <select id="yTopicField" className="form-select" name="yTopic" value={formFields.yTopic} onChange={handleChangeYTopic}  onBlur={saveYDataset} placeholder="Topic">
                        {
                            yTopicList.current.map(yTopic=>
                                <option value={yTopic}>{yTopic}</option>
                            )
                        }
                    </select>
                </div>
                <div className="mb-3 col">
                    <label htmlFor="yRecordField">Record Name</label>
                    <select id="yRecordField" className="form-select" name="yRecord" value={formFields.yRecord} onChange={handleChange} onBlur={saveYDataset} placeholder="Record">
                        {
                            yRecordList.map(yRecord=>
                                <option value={yRecord}>{yRecord}</option>
                            )
                        }

                    </select>
                </div>
            </div>
        </fieldset>
    )


}
export default YDatasetForm