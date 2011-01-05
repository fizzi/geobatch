/*
 *  GeoBatch - Open Source geospatial batch processing system
 *  http://code.google.com/p/geobatch/
 *  Copyright (C) 2007-2008-2009 GeoSolutions S.A.S.
 *  http://www.geo-solutions.it
 *
 *  GPLv3 + Classpath exception
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package it.geosolutions.geobatch.octave.actions.templates.FileInFileOut;

import it.geosolutions.geobatch.octave.SheetBuilder;
import it.geosolutions.geobatch.octave.OctaveCommand;
import it.geosolutions.geobatch.octave.OctaveExecutableSheet;
import it.geosolutions.geobatch.octave.OctaveFunctionFile;
import it.geosolutions.geobatch.octave.SerializableOctaveFile;
import it.geosolutions.geobatch.octave.SerializableOctaveObject;

import java.util.ArrayList;
import java.util.List;

import dk.ange.octave.exception.OctaveEvalException;
import dk.ange.octave.exception.OctaveException;

public class FileInFileOutSheetBuilder extends SheetBuilder {
    private final String filein,fileout;
    
    /**
     * 
     * @param file_in value of the input file (absolute path)
     * @param file_out value of the output file (absolute path)
     */
    public FileInFileOutSheetBuilder(String file_in,String file_out){
        super();
        filein=file_in;
        fileout=file_out;
    }
    
    /**
     * The prototype of the mars3d function is:
     * mars3d(file_in,file_out);
     */
    @Override
    protected OctaveExecutableSheet buildSheet(OctaveFunctionFile off) throws OctaveException{
        // returns should be an empty list
// TODO try to set this to null
        List<SerializableOctaveObject<?>> returns=off.getReturns();
        // to keep the builded function string (should be 1 element)
        List<OctaveCommand> commands=new ArrayList<OctaveCommand>();

        /**
         * Transforming function arguments to sheet 
         * variable definitions
         */
        List<SerializableOctaveObject<?>> arguments=off.getDefinitions();
        
        String script=off.getName();
        
        if (arguments!=null){
            if(arguments.size()==2){
                // setting value of arguments
                /**
                 * get the first variable definition which is supposed
                 * to be the first argument of the function
                 * function(file_in,file_out)
                 * and set its VALUE to the incoming file
                 * This will be transformed by the DefaultFunctionBuilder.preprocess
                 * into a sheet variable definition
                 */
                ((SerializableOctaveFile) arguments.get(0)).reSetVal(filein);
    
                /**
                 * get the second variable definition which is supposed
                 * to be the second argument of the function
                 * function(file_in,file_out)
                 * set its VALUE to the conventional string obtained by 
                 * buildFileName() method
                 * This will be transformed by the DefaultFunctionBuilder.preprocess
                 * into a sheet variable definition
                 */
                ((SerializableOctaveFile) arguments.get(1)).reSetVal(fileout);
            }
            else
                throw new OctaveEvalException("Argument list of "+off.getName()+
                " should contain at least 2 arguments!");
        
            /**
             * @note: Here we suppose that
             * getName returns serialized value which is
             * modified into the execute() method of the
             * FunctionAction class.
             * Variable Name should be substituted with
             * the file name it is representing:
             * arguments.get(0).getName() -> file_in
             * arguments.get(1).getName() -> file_out
             */
            script+="("+arguments.get(0).getName()+
                ","+arguments.get(1).getName()+");";
                
        } //endif arguments!=null
        else
            throw new OctaveEvalException("Argument list of "+off.getName()+" is empty!");
        
        commands.add(new OctaveCommand(script));
        
        // function arguments becomes sheet definitions
        // function returns becomes sheet returns
        return new OctaveExecutableSheet("FileIoFileOut_Sheet",commands,arguments,returns);
    }
}
