
import {Link, useParams} from 'react-router-dom';

import GoalCard from './goals/goal-card';
import useGoalList from '../../facades/hooks/useGoalList';

function GoalsDashboardPage(props){
    const {journalId} = useParams();
    const [goals] = useGoalList(["getAll"]);

    return (
        <div id="dashboardDiv" className="container page-div">
            <h2>Goals and Achievements</h2>
            <div className="row">
                <div className="col mb-3">
                    <Link className="btn btn-primary" to={"/"+journalId+"/goals/new"}>+Goal</Link>
                </div>
            </div>

            <br/><br/>
            
            {
                goals.map(goal=>
                    <GoalCard goal={goal}></GoalCard>
                )
            }
        </div>
    )
}

export default GoalsDashboardPage;