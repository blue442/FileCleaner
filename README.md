# FileCleaner
java program to clean EPA facility registry data

I download the national facility information from EPA (available at https://www.epa.gov/enviro/epa-state-combined-csv-download-files), but was having trouble importing some of the information (specifically the NATIONAL_FACILITY_FILE.CSV) to my postgres database. I tried a number of workarounds to address the line end character issues (i.e. https://kb.iu.edu/d/acux) but had little to no luck, so with a little assitance created a system that reads the file line by line and then rewrites it with system-correct line ending characters. 

Along the way, we also addressed a number of data cleaning issues having to do with extraneous carriage returns, line feeds, and null characters.
