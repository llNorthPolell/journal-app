export function processDashboardWidgets(config, journalEntriesList) {
    if (config==null){
        console.log("Error: config is undefined.");
        return;
    } 

    let processedConfigs = [];

    function generateLastEntry(){
        return (journalEntriesList!=null && journalEntriesList.length>0)? 
            journalEntriesList[journalEntriesList.length-1].overview
            : "You have no journal entries at the moment. Please create one.";
    }

    function generateLineGraph(widget){
        let payload = {
            title : widget.title,
            labels : widget.labels,
            data : {
                x: [],
                y: [...widget.data.yValues]
            }
        };

        let xValues=[];
        let yValues=[];

        journalEntriesList.forEach(
            journalEntry => {
                const journalBodyItem = journalEntry.journalBodyItems.find(
                    journalBodyItem=> journalBodyItem.topic === widget.data.yValues[0].topic
                );

                if (journalBodyItem == undefined) return;
                const record = journalBodyItem.recordList.find(
                    record=> record.key===widget.data.yValues[0].record
                );
                
                if (record == undefined) return;
                xValues.push(journalEntry[widget.data.xValue]);
                yValues.push(record.value);

            }
        );

        console.log("xValues: " + xValues);
        console.log("yValues: " + yValues);
        payload.data.x = [...xValues];
        payload.data.y[0].data=[...yValues];
        return payload;
    }


    config.map(
        widget=> {
            if(widget.type==="last-entry")
                widget.payload = generateLastEntry();
            else if (widget.type==="line-graph"){
                widget.payload = generateLineGraph(widget);
            }

            if (widget.payload!=null)
                processedConfigs.push(widget);
        }
    )

    return processedConfigs;
}

