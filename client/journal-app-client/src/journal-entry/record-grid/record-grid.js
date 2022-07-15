import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPlus } from '@fortawesome/free-solid-svg-icons';
import {v4 as uuidv4} from 'uuid';
import listUtil from '../../util/functions/list-util';
import RecordFieldGroup from "../record-field-group/record-field-group";

function RecordGrid (props) {
    const handleInsertNewRecord = e => {
        e.preventDefault();
        listUtil(props.recordList,props.setRecordList,{type:"INSERT",payload:{id:uuidv4(), key:"", value:""}});
    }

    return (      
        <div>
            <div className="row">
                <div className="col">
                    <label id="recordGridLabel" htmlFor="recordsDiv" className="form-label">Records</label>
                </div>
                {
                    (props.mode=="VIEW")?
                        null
                    : 
                        <div className="col col-md-4">
                            <button id="newRecordBtn" className="btn link-primary" onClick={handleInsertNewRecord}><FontAwesomeIcon icon={faPlus} /></button>
                        </div>
                        
                }
            </div>

            <br/>

            <div id="recordsDiv">
                {
                    props.recordList.map(
                        record => (
                            <RecordFieldGroup 
                                key={record.id} 
                                data={record} 
                                setRecordList={props.setRecordList} 
                                recordList={props.recordList}
                                mode={props.mode}></RecordFieldGroup>
                        )
                    )
                }
            </div>
        </div>             
    );
}
export default RecordGrid;