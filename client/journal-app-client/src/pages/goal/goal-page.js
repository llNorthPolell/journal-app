import {Link, useParams, useNavigate} from 'react-router-dom';

import { DefaultGoal } from './goal-dto';

import GoalForm from './goal-form';
import useJournalList from '../../facades/hooks/useJournalList';
import useGoalList from '../../facades/hooks/useGoalList';
import useSession from '../../facades/hooks/useSession';


function GoalPage (props){
    const navigate = useNavigate();
    const {journalId,entryId} = useParams();
  
    const [currentJournal, currentGoal]=useSession(["currentJournal","currentGoal"]);


    const [updateJournal] = useJournalList(["update"]);
    const [createGoal] = useGoalList(["insert"]);


    async function submitNew(formFields) {
        const output = { ...formFields, journal: journalId };

        const returnGoal = await createGoal(output);
        console.log("Published " + JSON.stringify(returnGoal) + " to " + journalId);

        updateJournal(journalId, {
            last_updated: new Date().toISOString()
        });

        navigate('/' + journalId + "/goals");
    }


    async function submitUpdate(formFields) {
        const output = { ...formFields };

        console.log("Updating goal with " + JSON.stringify(output));
        //const returnGoal = await updateGoal(entryId, output);
        //console.log("Published " + JSON.stringify(returnGoal) + " to " + journalId);
        
        navigate('/' + journalId + "/goals");
    }

    return (
        <div id="goalFormDiv" className="container page-div">
          <div>
            <Link id="toGoalMenuButton" className="btn btn-outline-primary" to={"/"+journalId +"/goals"}>Go to Goals Dashboard</Link>
          </div>
          <br/><br/>
          {
            (props.mode==="NEW" || !currentGoal)?
                <GoalForm goal={DefaultGoal} mode={props.mode} submit={submitNew}/>
            :
                <GoalForm goal={currentGoal} mode={props.mode} submit={submitUpdate}/>
    
          }
          
    
        </div>
    );
}

export default GoalPage;