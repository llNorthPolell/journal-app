import React, {useState,useEffect} from 'react';
import RemoveButton from '../../util/components/remove-button';
import useSimpleChildForm from '../../util/hooks/useSimpleChildForm';

function YDatasetForm (props){

    const [formFields,updateForm,saveYDataset,] = useSimpleChildForm(props.data, props.onChange);

    const [yTopicList,setYTopicList] = useState([]);
    const [yRecordList,setYRecordList]= useState([]);

    useEffect(()=>{
        if (props.currentJournal){
            const yTopicList = props.currentJournal.schemas.map(schema=>schema.topic);
            updateForm({yTopic:yTopicList[0]});
            setYTopicList(yTopicList); 
        }
        else setYTopicList([]);
    },[])

    useEffect(()=>{
        if (formFields.yTopic){
            const schema = props.currentJournal.schemas.find(schema=>schema.topic===formFields.yTopic)
            const yRecord =  schema.records[0];
            setYRecordList(schema.records);
            updateForm({yRecord:yRecord});
        } 
        else setYRecordList([]);
    },[formFields.yTopic])


    const handleChange = e => {
        updateForm({[e.target.name]: e.target.value});
    }

    const handleChangeYTopic = e => {
        let newYRecord=formFields.yRecord;
        if (props.currentJournal){
            const schema = props.currentJournal.schemas.find(schema=>schema.topic===e.target.value)
            newYRecord =  schema.records[0];
        }
        updateForm({ytopic: e.target.value, yRecord: newYRecord});
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
                            yTopicList.map(yTopic=>
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