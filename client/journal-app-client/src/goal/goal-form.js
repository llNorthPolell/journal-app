import React, {useState,useRef} from 'react';
import SimpleInput from '../util/components/simple-input';
import useSimpleState from '../util/hooks/useSimpleState';
import { DefaultGoal } from './goal-dto';

function GoalFormPage(props) {

    const [description, setDescription, handleChangeDescription] = useSimpleState();
    return (
        <div className="pageDiv container">
            <form>
                


            </form>
        </div>
    )
}
export default GoalFormPage