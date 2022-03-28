import React from 'react';

import RemoveButton from '../util/components/remove-button'

function JournalBodyItem(props){
    const remove = (id) => {
        props.removeFromUsedTopics(props.topic);
        props.removeFromBody(id);
    }

    return(
        <div>
            <div className="card">
                <div className="card-header">
                    <div className="row">
                        <div className="col">
                            <h5 className="card-title">{props.topic}</h5>
                        </div>
                        <RemoveButton id={props.id} remove={remove} moreClasses="float-end"></RemoveButton>
                    </div>   
                </div>
                <div className="card-body">
                    <p className="card-text">{props.comment}</p>
                    {
                        props.records.map(record => (
                            <div id={"Record_"+record.id} key={record.id} className="container row mb-3">
                                <div className="col">
                                    <input id={"recordKeyField_"+record.id} className="form-control" defaultValue={record.key} disabled/>
                                </div>
                                <div className="col">
                                    <input id={"recordValueField_"+record.id} className="form-control" defaultValue={record.value} disabled/>
                                </div>     
                            </div>
                        ))
                    }
                </div>
            </div>
            <br/>
        </div>
    );

}
export default JournalBodyItem;
