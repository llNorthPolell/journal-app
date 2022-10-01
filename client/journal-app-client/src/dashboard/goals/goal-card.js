import {Link, useParams} from 'react-router-dom';

function GoalCard(props){
    const {journalId} = useParams();

    return (
        <div id="GoalCardDiv">
            {
                <div className="card">
                    <div className="card-header">
                        <div className="row">
                            <div className="col-md-10">
                                <h5>{props.goal.topic}</h5>
                            </div>
                        </div>
                    </div>
                    <div className="card-body">
                        <div className="card-text">
                            {props.goal.description}

                            <h5>Conditions:</h5>
                            <ul>
                                {
                                    props.goal.objectives.map(objective=>
                                        <li>
                                            <div className="card">
                                                <div className="card-header">
                                                    <h5>{objective.name}</h5>
                                                </div>
                                                <div className="card-body">
                                                    <div className="card-text">
                                                        <p>{objective.description}</p>
                                                        {
                                                            objective.targetList.map(target=>{
                                                                <p>{target.targetKey} {target.targetCondition} {target.targetValue}</p>
                                                            })
                                                        }
                                                    </div>
                                                </div>
                                            </div>
                                        </li>
                                    )
                                }
                            </ul>
                            <Link className="stretched-link" to={"/" + journalId + "/goals/" + props.goal.key}></Link>
                        </div>
                    </div>
                </div>
            }
        </div>
    );
}
export default GoalCard;