package com.demo.app;




import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.GlobalSimilarityFunction;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;
import es.ucm.fdi.gaia.jcolibri.casebase.LinealCaseBase;
import es.ucm.fdi.gaia.jcolibri.exception.InitializingException;
import es.ucm.fdi.gaia.jcolibri.cbraplications.StandardCBRApplication;
import es.ucm.fdi.gaia.jcolibri.cbrcore.*;
import es.ucm.fdi.gaia.jcolibri.exception.OntologyAccessException;
import es.ucm.fdi.gaia.jcolibri.connector.DataBaseConnector;
import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;
import es.ucm.fdi.gaia.jcolibri.util.FileIO;
import es.ucm.fdi.gaia.jcolibri.exception.InitializingException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.RetrievalResult;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.selection.SelectCases;
import es.ucm.fdi.gaia.jcolibri.util.FileIO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;




public  class Test4 implements StandardCBRApplication {

	Connector _connector;
	CBRCaseBase _caseBase;
	//ArrayList<RetrievalResult>cases;
//	public Collection<CBRCase> casestoreturn;
	//public collection<RetrievalResult>evaltogather;

	//public CBRCaseBase mycases;
	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.BasicCBRApplication#configure()
	 */
	public void configure() throws ExecutionException {
		try {
			_connector = new DataBaseConnector();
			//_connector.initFromXMLfile(jcolibri.util.FileIO.findFile("com.demo.app.databaseconfig.xml"));
			_connector.initFromXMLfile(es.ucm.fdi.gaia.jcolibri.util.FileIO.findFile("com/demo/app/databaseconfig.xml"));
			_caseBase = (CBRCaseBase) new LinealCaseBase();
		}catch(Exception e) {
			throw new ExecutionException(e);
		}
		}

		//Collection <CBRCase> mycases;

	
	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.BasicCBRApplication#preCycle()
	 */
	public CBRCaseBase preCycle() throws ExecutionException {

		_caseBase.init(_connector);
		//java.util.Collection<CBRCase> cases = _caseBase.getCases();
		System.out.println("precycle complete");

		return _caseBase;
	}


		//} catch (InitializingException e){
		//	e.printStacTrace();
		//}
		//for (CBRCase c : _caseBase.getCases())
		//	System.out.Println(c);
		//return _caseBase;}
	
	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.BasicCBRApplication#cycle()
	 */
	public void cycle(CBRQuery cbrQuery)
	{		
		/*//******** NumericSim Retrieval **********/
		
		NNConfig simConfig = new NNConfig();
		simConfig.setDescriptionSimFunction(new Average());
		simConfig.addMapping(new Attribute("Accommodation",com.demo.app.TravelDescription.class),
				new Equal());
		Attribute duration = new Attribute("Duration",com.demo.app.TravelDescription.class);
		simConfig.addMapping(duration, new Interval(31));
		simConfig.setWeight(duration, 0.5);
		simConfig.addMapping(new Attribute("HolidayType",com.demo.app.TravelDescription.class), new Equal());
		simConfig.addMapping(new Attribute("NumberOfPersons",com.demo.app.TravelDescription.class), new Equal());
		
		simConfig.addMapping(new Attribute("Region",  com.demo.app.TravelDescription.class), new Average());
		simConfig.addMapping(new Attribute("region", com.demo.app.Region.class), new Equal());
		simConfig.addMapping(new Attribute("city",   com.demo.app.Region.class), new Equal());
		simConfig.addMapping(new Attribute("airport",  com.demo.app.Region.class), new Equal());
		simConfig.addMapping(new Attribute("currency",com.demo.app.Region.class), new Equal());

		//System.out.println(query);
		//System.out.println();
		//Blank description
		//query.setDescription((CaseComponent) new TravelDescription());
	//	   /********* Execute NN ************/
		Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(_caseBase.getCases(), cbrQuery, simConfig);
		//		Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(_caseBase.getCases(),
//				query,
//				simConfig);
		
	//	/********* Select cases **********/
		Collection<CBRCase> selectedcases = SelectCases.selectTopK(eval,3);
		System.out.println(selectedcases);

		//evaltogather=eval;
		//public void getMeMyCases( int k ) {
		//Collection<CBRCase> selectedcases = SelectCases.selectTopK( evaltogather, k);
		//casestoreturn = selectedcases;
		//to print the cases after i shortened them to what i want
		//System.out.println("Combined + " + k + " cases gathered");
		//for (CBRCase c : selectedcases)
		//	System.out.println(c);









		
//		/********* Reuse **********/
//		// Compute a direct proportion between the "NumberOfPersons" and "Price" attributes.
//		NumericDirectProportionMethod.directProportion(	new Attribute("NumberOfPersons",TravelDescription.class),
//													 	new Attribute("price",TravelSolution.class),
//													 	query, selectedcases);
//		// Compute a direct proportion between the "Duration" and "Price" attributes.
//		NumericDirectProportionMethod.directProportion(	new Attribute("Duration",TravelDescription.class),
//			 											new Attribute("price",TravelSolution.class),
//			 											query, selectedcases);
//
//
//		Collection<CBRCase> newcases = CombineQueryAndCasesMethod.combine(query, selectedcases);
//		System.out.println("Combined cases");
//		for(jcolibri.cbrcore.CBRCase c: newcases)
//			System.out.println(c);
//
//		/********* Revise **********/
//		CBRCase bestCase = newcases.iterator().next();
//
//		HashMap<Attribute, Object> componentsKeys = new HashMap<Attribute,Object>();
//		componentsKeys.put(new Attribute("caseId",TravelDescription.class), "test3id");
//		componentsKeys.put(new Attribute("id",TravelSolution.class), "test3id");
//		//componentsKeys.put(new Attribute("id",Region.class), 7);
//		jcolibri.method.revise.DefineNewIdsMethod.defineNewIdsMethod(bestCase, componentsKeys);
//
//		System.out.println("Case with new Id");
//		System.out.println(bestCase);
		
		 // /********* Retain **********/
		
//		 Uncomment next line to store cases into persistence
		//jcolibri.method.retain.StoreCasesMethod.storeCase(_caseBase, bestCase);
	}

	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.BasicCBRApplication#postCycle()
	 */
	public void postCycle() throws ExecutionException {
		this._caseBase.close();

	}

	/**
	 // * @param args
	 */
	public static void main(String[] args)  {
		try{
		TravelDescription queryDesc = new TravelDescription();
		queryDesc.setAccommodation(TravelDescription.AccommodationTypes.ThreeStars);
		queryDesc.setDuration(10);
		queryDesc.setHolidayType(new String("Recreation"));
		queryDesc.setNumberOfPersons(4);

		Region region = new Region();
		region.setRegion(new String("Bulgaria"));
		region.setCity(new String("Sofia"));
		region.setCurrency(new String("Euro"));
		region.setAirport(new String("airport"));
		queryDesc.setRegion(region);
		CBRQuery query = new CBRQuery();
		query.setDescription( (CaseComponent) queryDesc);

		Test4 test = new Test4();
		//@Override
		//public void postCycle() throws ExecutionException {

		//}
		//}; //{
		//     @Override
		//      public void postCycle() throws ExecutionException {

		//  }
		//}; //{
		test.configure();
		test.preCycle();
		test.cycle(query);
		test.postCycle();
		//  }
		// test4.preCycle();
		//  test4.cycle(query);
		System.out.println("Cycle finished. Type exit to idem");
		//}while(!reader.readLine().equals("exit"));
		//}
	}catch (ExecutionException e) {
		System.out.println(e.getMessage());
		e.printStackTrace();
	}//
 //catch (Exception e) {
		// TODO Auto-generated catch block
	//	e.printStackTrace();
	}
}














//Query definition