#include <stdio.h>
#include <stdlib.h>
#include <windows.h>
#include <dirent.h>

int main(int argc, char *argv[]) {
    
    if (argc != 2) 
        return 1;

    const char *path = argv[1]; 
    int result;

    wchar_t* wPath = (wchar_t*)malloc((strlen(path) + 1) * sizeof(wchar_t));

    for (size_t i = 0; i <= strlen(path); ++i)
        wPath[i] = (wchar_t)path[i];

    result = SystemParametersInfoW(SPI_SETDESKWALLPAPER, 0, (void *)wPath, SPIF_UPDATEINIFILE | SPIF_SENDWININICHANGE);

    if (result)
        return 0;

    return 1;

}